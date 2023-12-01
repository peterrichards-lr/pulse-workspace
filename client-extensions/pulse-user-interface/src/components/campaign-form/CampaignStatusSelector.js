import ClayForm, {ClaySelect} from '@clayui/form';
import {buildGraphQlQuery, parseGraphQlQueryResponse} from "../../common/utility";
import {useEffect, useState} from "react";
import baseFetch from "../../common/services/liferay/api";
import {Liferay} from "../../common/services/liferay/liferay";
import ClaySelectController from "../react-hook-form/ClaySelectController";

const GRAPHQL_PATH = '/o/graphql';

const CampaignStatusSelector = ({
                                    campaignStatusListTypeErc,
                                    setValue,
                                    control,
                                    defaultValue,
                                }) => {

    const controlName = "campaignStatus"
    const [options, setOptions] = useState([]);

    useEffect(async () => {
        const campaignStatuses = buildGraphQlQuery(
            'listTypeDefinitionByExternalReferenceCode',
            'listTypeEntries { name, key }',
            {
                externalReferenceCode: `"${campaignStatusListTypeErc}"`
            }
        );

        await baseFetch(GRAPHQL_PATH, {
            method: 'POST',
            body: campaignStatuses
        }).then(
            (campaignStatusesResponse) => {
                const {listTypeEntries} = parseGraphQlQueryResponse(
                    'listTypeDefinitionByExternalReferenceCode',
                    campaignStatusesResponse
                );
                if (listTypeEntries === undefined || !(listTypeEntries instanceof Array)) {
                    console.warn('listTypeEntries is not an array');
                    return;
                }
                console.debug(`Found ${listTypeEntries.length} option(s)`);
                setOptions(listTypeEntries);
            }
        ).catch((reason) => console.error(reason));
    }, []);

    useEffect(() => {
        if (options.length > 0) {
            if (defaultValue) {
                setValue(controlName, defaultValue)
                return
            }
            const value = options.at(0)?.friendlyUrlPath
            console.log(`${Liferay.Language.get('campaign-status')} default`, value)
            setValue(controlName, value)
        }
    }, [options])

    return (
        <ClayForm.Group>
            <ClaySelectController
                name={controlName}
                label={Liferay.Language.get('campaign-status')}
                control={control}
                required={true}
            >
                {options.map(item => (
                    <ClaySelect.Option
                        key={item.key}
                        label={item.name}
                        value={item.key}
                    />
                ))}
            </ClaySelectController>
        </ClayForm.Group>
    );
};

export default CampaignStatusSelector