#!/bin/zsh
setopt no_nomatch
rm -rf ./bundles/data/elasticsearch7
rm -rf ./bundles/data/hypersonic
rm -f ./bundles/osgi/client-extensions/*
rm -rf ./bundles/osgi/state/*
setopt nomatch