#!/usr/bin/python -B

"""Regenerates (just) ICU data source files used to build ICU data files."""

from __future__ import print_function

import os
import shutil
import subprocess
import sys

import i18nutil
import icuutil


# Run with no arguments from any directory, with no special setup required.
# See icu4c/source/data/cldr-icu-readme.txt for the upstream ICU instructions.
def main():
  cldr_dir = icuutil.cldrDir()
  print('Found cldr in %s ...' % cldr_dir)
  icu_dir = icuutil.icuDir()
  print('Found icu in %s ...' % icu_dir)

  # Ant doesn't have any mechanism for using a build directory separate from the
  # source directory so this build script creates a temporary directory and then
  # copies all necessary ICU4J and CLDR source code to here before building it:
  i18nutil.SwitchToNewTemporaryDirectory()
  build_dir = os.getcwd()
  cldr_build_dir = os.path.join(build_dir, 'cldr')
  icu4c_build_dir = os.path.join(build_dir, 'icu4c')
  icu4j_build_dir = os.path.join(build_dir, 'icu4j')
  icu_tools_build_dir = os.path.join(build_dir, 'icu_tools')

  print('Copying CLDR source code ...')
  shutil.copytree(cldr_dir, cldr_build_dir, symlinks=True)
  print('Copying ICU4C source code ...')
  shutil.copytree(os.path.join(icu_dir, 'icu4c'), icu4c_build_dir, symlinks=True)
  print('Copying ICU4J source code ...')
  shutil.copytree(os.path.join(icu_dir, 'icu4j'), icu4j_build_dir, symlinks=True)
  print('Copying ICU tools source code ...')
  shutil.copytree(os.path.join(icu_dir, 'tools'), icu_tools_build_dir, symlinks=True)

  # Setup environment variables for all subshell
  os.environ['ANT_OPTS'] = '-Xmx4096m'

  # This is the location of the original CLDR source tree (not the temporary
  # copy of the tools source code) from where the data files are to be read:
  os.environ['CLDR_DIR'] = cldr_build_dir #os.path.join(os.getcwd(), 'cldr')

  os.environ['ICU4C_ROOT'] = icu4c_build_dir
  os.environ['ICU4J_ROOT'] = icu4j_build_dir
  os.environ['TOOLS_ROOT'] = os.path.join(icu_dir, 'tools')
  cldr_tmp_dir = os.path.join(build_dir, 'cldr-staging')
  os.environ['CLDR_TMP_DIR'] = cldr_tmp_dir

  print('Building CLDR tools ...')
  os.chdir(os.path.join(cldr_build_dir, 'tools', 'java'))
  subprocess.check_call(['ant', 'clean'])
  subprocess.check_call(['ant', 'all'])
  subprocess.check_call(['ant', 'jar'])

  # As per tools/cldr/cldr-to-icu/lib/README.txt we need to ensure local maven
  # repository contains both cldr.jar and utilities.jar
  icu_tools_lib_dir = os.path.join(icu_tools_build_dir, 'cldr', 'cldr-to-icu', 'lib')
  cldr_jar = os.path.join(cldr_build_dir, 'tools', 'java', 'cldr.jar')
  utilities_jar = os.path.join(cldr_build_dir, 'tools', 'java', 'libs', 'utilities.jar')

  os.chdir(icu_tools_lib_dir)
  subprocess.check_call([
    'mvn',
    'install:install-file',
    '-DgroupId=org.unicode.cldr',
    '-DartifactId=cldr-api',
    '-Dversion=0.1-SNAPSHOT',
    '-Dpackaging=jar',
    '-DgeneratePom=true',
    '-DlocalRepositoryPath=.',
    '-Dfile={cldr_jar}'.format(cldr_jar=cldr_jar)
  ])

  subprocess.check_call([
      'mvn',
      'install:install-file',
      '-DgroupId=com.ibm.icu',
      '-DartifactId=icu-utilities',
      '-Dversion=0.1-SNAPSHOT',
      '-Dpackaging=jar',
      '-DgeneratePom=true',
      '-DlocalRepositoryPath=.',
      '-Dfile={utilities_jar}'.format(utilities_jar=utilities_jar)
  ])

  os.chdir(os.path.join(icu_tools_build_dir, 'cldr', 'cldr-to-icu'))
  subprocess.check_call([
      'mvn',
      'dependency:purge-local-repository',
      '-DsnapshotsOnly=true'
  ])

  icu4c_data_build_dir = os.path.join(icu4c_build_dir, 'source/data')
  os.chdir(icu4c_data_build_dir)
  subprocess.check_call(['ant', 'cleanprod'])
  subprocess.check_call(['ant', 'setup'])
  subprocess.check_call(['ant', 'proddata'])

  os.chdir(os.path.join(icu_tools_build_dir, 'cldr', 'cldr-to-icu'))

  cldr_production_data_build_dir = os.path.join(build_dir, 'proddata')
  os.mkdir(cldr_production_data_build_dir)

  # Finally we "compile" CLDR-data to a "production" form and place it in ICU
  # Temporarily redefining CLDR_DIR per cldr-icu-readme.txt
  build_production_data_env = os.environ.copy()
  build_production_data_env["CLDR_DIR"] = os.path.join(cldr_tmp_dir, 'production')
  subprocess.check_call([
      'ant',
      '-f',
      'build-icu-data.xml',
      '-DoutDir=' + cldr_production_data_build_dir,
      '-DincludePseudoLocales=true'
  ], env=build_production_data_env)

  icu4c_data_source_dir = os.path.join(icu_dir, 'icu4c/source/data')
  subprocess.check_call([
      'rsync',
      '-a',
      '{src}/'.format(src=cldr_production_data_build_dir),
      '{dst}'.format(dst=icu4c_data_source_dir)
  ])

  print('Look in %s for new data source files' % icu4c_data_source_dir)
  sys.exit(0)

if __name__ == '__main__':
  main()
