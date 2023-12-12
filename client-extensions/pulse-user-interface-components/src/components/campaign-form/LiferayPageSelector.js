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
                                 errors,
                                 spriteMap
                             }) => {
    const controlName = "targetUrl"
    const [options, setOptions] = useState([]);

    useEffect(() => {
        const liferayPages = buildGraphQlQuery(
            'sitePages',
            'items { title, friendlyUrlPath }',
            {
                siteKey: `"${Liferay.ThemeDisplay.getSiteGroupId()}"`
            }
        );

        baseFetch(GRAPHQL_PATH, {
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
        ).catch((reason) => console.error(reason))
    }, [])

    useEffect(() => {
        if (options.length > 0) {
            if (defaultValue) {
                setValue(controlName, defaultValue)
                return
            }
            const value = options.at(0)?.friendlyUrlPath
            console.debug(`${Liferay.Language.get('target-url')} default`, value)
            setValue(controlName, value)
        }
    }, [options, defaultValue, setValue])

    return (
        <ClayForm.Group className={`${errors[controlName] ? "has-error" : ""}`}>
            <ClaySelectController
                name={controlName}
                label={Liferay.Language.get('target-url')}
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
            {errors[controlName] && <ClayForm.FeedbackItem>
                <ClayForm.FeedbackIndicator
                    spritemap={spriteMap}
                    symbol="exclamation-full"
                />
                {`The ${controlName} is invalid.`}
            </ClayForm.FeedbackItem>}
        </ClayForm.Group>
    );
};

export default LiferayPageSelector