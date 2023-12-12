import {Liferay} from "../../common/services/liferay/liferay";
import ClayForm, {ClayInput} from '@clayui/form';
import ClayPanel from "@clayui/panel";

const UtmDetails = ({spriteMap, register}) => {
    return (
        <ClayPanel
            collapsable
            displayTitle={Liferay.Language.get('utm-data')}
            displayType="unstyled"
            showCollapseIcon={true}
            spritemap={spriteMap}
        >
            <ClayPanel.Body>
                <ClayForm.Group>
                    <label htmlFor="utmContent">{Liferay.Language.get('content')}</label>
                    <ClayInput
                        type="text"
                        {...register("utmContent")}
                    />
                </ClayForm.Group>
                <ClayForm.Group>
                    <label htmlFor="utmMedium">{Liferay.Language.get('medium')}</label>
                    <ClayInput
                        type="text"
                        {...register("utmMedium")}
                    />
                </ClayForm.Group>
                <ClayForm.Group>
                    <label htmlFor="utmSource">{Liferay.Language.get('source')}</label>
                    <ClayInput
                        type="text"
                        {...register("utmSource")}
                    />
                </ClayForm.Group>
                <ClayForm.Group>
                    <label htmlFor="utmTerm">{Liferay.Language.get('term')}</label>
                    <ClayInput
                        type="text"
                        {...register("utmTerm")}
                    />
                </ClayForm.Group>
            </ClayPanel.Body>
        </ClayPanel>
    )
}

export default UtmDetails