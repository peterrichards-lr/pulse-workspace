import {useEffect, useState} from 'react';
import {Liferay} from '../../common/services/liferay/liferay';
import CampaignDataGrid from '../campaign-data-grid/CampaignDataGrid';
import CampaignGraph from '../campaign-graph/CampaignGraph';
import baseFetch from '../../common/services/liferay/api';

const LIFERAY_HOST =
    process.env.REACT_APP_LIFERAY_HOST || window.location.origin;
const CAMPAIGN_API_PATH = '/o/c/campaigns/';
const CAMPAIGN_INTERACTION_API_PATH = '/o/c/campaigninteractions/';
const HEADER_CONTENT_TYPE_JSON = 'application/json';

const UserJourney = ({clientExternalReferenceCode}) => {
    const [errorMessage, setErrorMessage] = useState();
    const [rows, setRows] = useState([]);

    useEffect(() => {
        if (!(Liferay || Liferay.ThemeDisplay)) {
            setErrorMessage('This component needs to be run within Liferay');
            return;
        }

        if (!(Liferay.ThemeDisplay.isSignedIn || !clientExternalReferenceCode)) {
            setErrorMessage(
                'The external reference code of the OAuth 2 client has not been provided'
            );
            return;
        }

        let liferayFetch;
        if (clientExternalReferenceCode) {
            const oAuth2Client = Liferay.OAuth2Client.FromUserAgentApplication(
                clientExternalReferenceCode
            );
            if (!oAuth2Client) {
                setErrorMessage(
                    `Unable to find the OAuth2 client for ${clientExternalReferenceCode}`
                );
                return;
            }

            var headers = {
                HEADER_CONTENT_TYPE: HEADER_CONTENT_TYPE_JSON,
            };

            liferayFetch = (url) => {
                return oAuth2Client.fetch(url, headers);
            };
        } else {
            liferayFetch = (url) => {
                return baseFetch(url);
            };
        }

        var queryString = 'pageSize=100';
        liferayFetch(new URL(`${CAMPAIGN_API_PATH}?${queryString}`, LIFERAY_HOST))
            .then((response) => {
                const {items, pageSize, totalCount} = response;
                if (items === undefined || !(items instanceof Array)) {
                    console.warn('Items is not an array');
                    return;
                }
                if (pageSize < totalCount) {
                    console.warn(
                        `The returned set of items is not the full set: returned ${pageSize}, set size ${totalCount}`
                    );
                }
                if (items.length !== pageSize) {
                    console.debug(
                        `There are fewer items than requested: requested: returned ${items.length}, requested ${pageSize}`
                    );
                }
                return items;
            })
            .then((campaigns) => {
                if (!campaigns) {
                    console.warn('Unable to retrieve campaigns');
                    return;
                }
                Promise.all(
                    campaigns.map((campaign) => {
                        const filter = `r_interaction_c_campaignId eq '${campaign.id}'`;
                        return liferayFetch(
                            new URL(
                                `${CAMPAIGN_INTERACTION_API_PATH}?${queryString}&filter=${filter}`,
                                LIFERAY_HOST
                            )
                        )
                            .then((response) => {
                                const {items, pageSize, totalCount} = response;
                                if (items === undefined || !(items instanceof Array)) {
                                    console.warn('Items is not an array');
                                    return;
                                }
                                if (pageSize < totalCount) {
                                    console.warn(
                                        `The returned set of items is not the full set: returned ${pageSize}, set size ${totalCount}`
                                    );
                                }
                                if (items.length !== pageSize) {
                                    console.debug(
                                        `There are fewer items than requested: requested: returned ${items.length}, requested ${pageSize}`
                                    );
                                }
                                return items;
                            })
                            .then((campaignInteractions) => {
                                console.debug(
                                    `campaignInteractions for ${campaign.name} [${campaign.id}]`,
                                    campaignInteractions
                                );
                                if (campaignInteractions && campaignInteractions.length > 0) {
                                    const latestTimestamp = campaignInteractions.reduce(
                                        (acc, it) => {
                                            acc[it.event] =
                                                acc[it.event] === undefined
                                                    ? it.dateCreated
                                                    : acc[it.event].dateCreated > it.dateCreated
                                                        ? acc[it.event]
                                                        : it.dateCreated;
                                            return acc;
                                        },
                                        {}
                                    );
                                    const eventCount = campaignInteractions.reduce((acc, it) => {
                                        acc[it.event] =
                                            acc[it.event] === undefined ? 1 : (acc[it.event] += 1);
                                        return acc;
                                    }, {});
                                    const data = campaignInteractions.map((ci) => {
                                        return {
                                            campaign: campaign.name,
                                            sourceMedium: `${
                                                campaign.uTMSource ? campaign.uTMSource : 'unknown'
                                            } / ${
                                                campaign.uTMMedium ? campaign.uTMMedium : 'unknown'
                                            }`,
                                            event: ci.event,
                                            count: eventCount[ci.event],
                                            lastUpdated: latestTimestamp[ci.event],
                                        };
                                    });
                                    var dataArr = data.map((item) => {
                                        return [item.event, item];
                                    });
                                    var mapArray = new Map(dataArr);
                                    return [...mapArray.values()];
                                } else {
                                    return [];
                                }
                            })
                            .catch((reason) => console.error(reason));
                    })
                )
                    .then((campaignData) => {
                        const flattened = campaignData.flat(1);
                        flattened.forEach((row, i) => (row.key = i));
                        console.debug('flattened', flattened);
                        setRows(flattened);
                    })
                    .catch((reason) => console.error(reason));
            });
    }, [clientExternalReferenceCode]);

    let errorElement;
    let reportElement;

    if (errorMessage) {
        errorElement = (
            <div className="alert alert-warning">
                <div role="alert" className="alert-autofit-row autofit-row">
                    <div className="autofit-col">
                        <div className="autofit-section">
              <span className="alert-indicator">
                <svg
                    className="lexicon-icon lexicon-icon-warning-full"
                    role="presentation"
                    viewBox="0 0 512 512"
                >
                  <path
                      className="lexicon-icon-outline"
                      d="M506.3,409.3l-214-353.7c-16.8-30.6-55.8-32.6-72.6,0L5.7,409.3C-8,436.5,5,480,42,480h428C506,480,522.5,436.5,506.3,409.3z M224,392c0-42.5,64-42,64,0C288,433.5,224,434.5,224,392z M288,288c0,42.5-64,40.5-64,0c0-20.4,0-83.6,0-104c0-43,64-43.5,64,0C288,204.4,288,267.6,288,288z"
                  ></path>
                </svg>
              </span>
                        </div>
                    </div>
                    <div className="autofit-col autofit-col-expand">
                        <div className="autofit-section">
                            <strong className="lead">Warning</strong>
                            {errorMessage}
                        </div>
                    </div>
                </div>
            </div>
        );
    } else {
        reportElement = (
            <>
                <CampaignGraph data={rows}/>
                <CampaignDataGrid rows={rows}/>
            </>
        );
    }
    return (
        <>
            {reportElement}
            {errorElement}
        </>
    );
};

export default UserJourney;
