#!/bin/zsh
LIFERAY_TAG_DEFAULT=$(curl -s 'https://hub.docker.com/v2/repositories/liferay/dxp/tags?page_size=1024' | jq '.results[] | select(.name|test("^7.4")) | select(.name|test("^([0-9].[0-9].[0-9]+)-(u|sp|dxp-|de-)[0-9]+$")) | .name' | head -n 1 | tr -d '"')
read -r "LIFERAY_TAG?Enter Liferay Docker Tag [$LIFERAY_TAG_DEFAULT] :"
LIFERAY_TAG="${LIFERAY_TAG:-$LIFERAY_TAG_DEFAULT}"

ADMIN_SCREEN_NAME_DEFAULT=test
read -r "ADMIN_SCREEN_NAME?Enter Administrator's screen name [$ADMIN_SCREEN_NAME_DEFAULT] :"
ADMIN_SCREEN_NAME="${ADMIN_SCREEN_NAME:-$ADMIN_SCREEN_NAME_DEFAULT}"

# Common
function common {
cp /Users/peterrichards/dev/docker/7.4-common/activation-key-dxpdevelopment-7.4-developeractivationkeys.xml ./bundles/deploy
printf "\n" >> ./bundles/portal-ext.properties
echo "default.admin.screen.name=${ADMIN_SCREEN_NAME_DEFAULT}" >> ./bundles/portal-ext.properties
}

# Update 101
function featureFlags_101 {
(
echo "feature.flag.LPS-164948=true"
echo "feature.flag.LPS-180328=true"
echo "feature.flag.LPS-180155=true"
echo "feature.flag.LPS-183727=true"
echo "feature.flag.LPS-182184=true"
echo "feature.flag.LPS-195205=true"
echo "feature.flag.LPS-189187=true"
echo "feature.flag.LPS-181663=true"
echo "feature.flag.LPS-148856=true"
echo "feature.flag.COMMERCE-11026=true"
echo "feature.flag.COMMERCE-11028=true"
echo "feature.flag.LPS-147671=true"
echo "feature.flag.LPS-161033=true"
echo "feature.flag.LPS-179035=true"
echo "feature.flag.LPS-180855=true"
echo "feature.flag.COMMERCE-10890=true"
echo "feature.flag.LPS-186558=true"
echo "feature.flag.COMMERCE-11287=true"
echo "feature.flag.LPS-187183=true"
) >> ./bundles/portal-ext.properties
exit
}

# Update 102
function featureFlags_102 {
(
echo "feature.flag.LPS-164948=true"
echo "feature.flag.LPS-180328=true"
echo "feature.flag.LPS-183727=true"
echo "feature.flag.LPS-182184=true"
echo "feature.flag.LPS-195205=true"
echo "feature.flag.LPS-165481=true"
echo "feature.flag.LPS-189187=true"
echo "feature.flag.LPS-181663=true"
echo "feature.flag.LPS-193884=true"
echo "feature.flag.LPS-148856=true"
echo "feature.flag.COMMERCE-11026=true"
echo "feature.flag.COMMERCE-11028=true"
echo "feature.flag.LPS-161033=true"
echo "feature.flag.LPS-179035=true"
echo "feature.flag.LPS-180855=true"
echo "feature.flag.COMMERCE-10890=true"
echo "feature.flag.LPS-186558=true"
echo "feature.flag.COMMERCE-11287=true"
echo "feature.flag.LPS-196935=true"
) >> ./bundles/portal-ext.properties
exit
}

case "$LIFERAY_TAG" in
  101)
    common
    featureFlags_101
    ;;
  102)
    common
    featureFlags_102
    ;;
  *) echo "Unknown version" ;;
esac
exit