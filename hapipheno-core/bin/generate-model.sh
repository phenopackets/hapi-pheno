#!/usr/bin/env bash
#set -x
set -e
set -u
set -o pipefail
set -o noclobber

# https://www.gnu.org/software/bash/manual/html_node/The-Shopt-Builtin.html
shopt -s nullglob

# stack overflow #59895
SOURCE="${BASH_SOURCE[0]}"
while [ -h "$SOURCE" ]; do
    DIR="$(cd -P "$(dirname "$SOURCE")" && pwd)"
    SOURCE="$(readlink "$SOURCE")"
    [[ $SOURCE != /* ]] && SOURCE="$DIR/$SOURCE"
done
DIR="$(cd -P "$(dirname "$SOURCE")" && pwd)"

cd "$DIR"/..

./mvnw -U exec:java \
    -Dexec.mainClass=com.essaid.model.dsl.Main \
    -Dexec.args="--dsl-source src/main/java/org/monarchinitiative/phenoex/model/model.modeldsl --out-dir src-gen/main/java"

