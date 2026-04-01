#!/bin/sh
# ----------------------------------------------------------------------------
# Licensed to the Apache Software Foundation (ASF) under one
# or more contributor license agreements.  See the NOTICE file
# distributed with this work for additional information
# regarding copyright ownership.  The ASF licenses this file
# to you under the Apache License, Version 2.0 (the
# "License"); you may not use this file except in compliance
# with the License.  You may obtain a copy of the License at
#
#    http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing,
# software distributed under the License is distributed on an
# "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
# KIND, either express or implied.  See the License for the
# specific language governing permissions and limitations
# under the License.
# ----------------------------------------------------------------------------

# ----------------------------------------------------------------------------
# Apache Maven Wrapper startup batch script, version 3.3.2
#
# Optional ENV vars
#   JAVA_HOME - location of a JDK home dir
#   MAVEN_OPTS - parameters passed to the Java VM when running Maven
# ----------------------------------------------------------------------------

set -euf
[ "${MVNW_VERBOSE-}" != true ] || set -x

# OS specific support.
native_path() { printf %s\\n "$1"; }
case "$(uname)" in
CYGWIN* | MINGW*)
  [ -z "${JAVA_HOME-}" ] || JAVA_HOME="$(cygpath --unix "$JAVA_HOME")"
  native_path() { cygpath --path --windows "$1"; }
  ;;
esac

# set JAVA_HOME
if [ -z "${JAVA_HOME-}" ]; then
  if [ -x /usr/libexec/java_home ]; then
    JAVA_HOME="$(/usr/libexec/java_home)" || :
  elif [ -d /usr/lib/jvm/default ]; then
    JAVA_HOME=/usr/lib/jvm/default
  elif [ -d /usr/lib/jvm/default-java ]; then
    JAVA_HOME=/usr/lib/jvm/default-java
  fi
fi

MAVEN_HOME="${MAVEN_HOME:-${M2_HOME:-}}"

if [ -z "${MAVEN_HOME-}" ]; then
  # shellcheck disable=SC2034
  MAVEN_HOME=~/.m2/wrapper/dists/apache-maven-3.9.9
fi

MVNW_REPOURL=${MVNW_REPOURL:-https://repo.maven.apache.org/maven2}
MVNW_WRAPPERURL="${MVNW_REPOURL}/org/apache/maven/wrapper/maven-wrapper/3.3.2/maven-wrapper-3.3.2.jar"
MVNW_DISTROURL="${MVNW_REPOURL}/org/apache/maven/apache-maven/3.9.9/apache-maven-3.9.9-bin.zip"

read_props() {
  wdir="${1-}"
  while [ -n "$wdir" ]; do
    pf="${wdir}/.mvn/wrapper/maven-wrapper.properties"
    if [ -f "$pf" ]; then
      while IFS='=' read -r key value; do
        case "${key-}" in
        distributionUrl) MVNW_DISTROURL="$value" ;;
        wrapperUrl) MVNW_WRAPPERURL="$value" ;;
        esac
      done <"$pf"
      break
    fi
    wdir="${wdir%/*}"
    [ "$wdir" != "$1" ] || break
  done
}
read_props "$(pwd)"

mvn_home_from_dist() {
  dist_dir="${MAVEN_HOME}"
  if [ ! -d "${dist_dir}/bin" ]; then
    dist_zip="${dist_dir}.zip"
    if [ ! -f "${dist_zip}" ]; then
      mkdir -p "${dist_dir%/*}"
      if command -v curl >/dev/null; then
        curl -fsSL -o "${dist_zip}" "${MVNW_DISTROURL}" || return
      elif command -v wget >/dev/null; then
        wget -q -O "${dist_zip}" "${MVNW_DISTROURL}" || return
      else
        echo >&2 "Error: curl or wget is required to download Maven"
        return 1
      fi
    fi
    if command -v unzip >/dev/null; then
      unzip -q -o -d "${dist_dir}" "${dist_zip}" || return
    else
      echo >&2 "Error: unzip is required to install Maven"
      return 1
    fi
    mv "${dist_dir}"/apache-maven-*/* "${dist_dir}/" 2>/dev/null || :
  fi
  printf %s "${dist_dir}"
}

MVN_HOME="$(mvn_home_from_dist)" || {
  echo >&2 "Error: could not download and install Maven"
  exit 1
}

exec "$(native_path "${MVN_HOME}/bin/mvn")" "$@"
