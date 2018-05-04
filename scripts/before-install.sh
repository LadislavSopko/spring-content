#!/usr/bin/env bash
echo Maven profile: ${MAVEN_PROFILE}
echo Build type: ${BUILD_TYPE}
echo Travis tag: ${TRAVIS_TAG}

#if [ -n "$TRAVIS_TAG" ]; then
#    export BUILD_TYPE=release
#else
#    export BUILD_TYPE=snapshot
#fi

sudo apt-get update
sudo apt-get install imagemagick

cat /etc/debian_version
cat /etc/redhat_version
exit