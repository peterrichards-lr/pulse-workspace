import ClayForm, {ClaySelect} from '@clayui/form';
import {buildGraphQlQuery, parseGraphQlQueryResponse} from "../../common/utility";
import {useEffect, useState} from "react";
import baseFetch from "../../common/services/liferay/api";

import {Liferay} from "../../common/services/liferay/liferay";
import ClaySelectController from "../react-hook-form/ClaySelectController";

const GRAPHQL_PATH = '/o/graphql';

const LiferayPageSelector = ({
                                 setValue,
                                 control,
                                 defaultValue,
                             }) => {
    const controlName = "targetPage"
    const [options, setOptions] = useState([]);

    useEffect(async () => {
        const liferayPages = buildGraphQlQuery(
            'sitePages',
            'items { title, friendlyUrlPath }',
            {
                siteKey: `"${Liferay.ThemeDisplay.getSiteGroupId()}"`
            }
        );

        await baseFetch(GRAPHQL_PATH, {
            method: 'POST',
            body: liferayPages
        }).then(
            (liferayPagesResponse) => {
                const {items} = parseGraphQlQueryResponse(
                    'sitePages',
                    liferayPagesResponse
                );
                if (items === undefined || !(items instanceof Array)) {
                    console.warn('items is not an array');
                    return;
                }
                console.debug(`Found ${items.length} option(s)`);
                setOptions(items);
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
                label={Liferay.Language.get('target-page')}
                control={control}
                required={true}
            >
                {options.map(item => (
                    <ClaySelect.Option
                        key={item.friendlyUrlPath}
                        label={`${item.title} [ ${item.friendlyUrlPath} ]`}
                        value={item.friendlyUrlPath}
                    />
                ))}
            </ClaySelectController>
        </ClayForm.Group>
    );
};

export default LiferayPageSelector