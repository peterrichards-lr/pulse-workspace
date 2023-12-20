import ClayIcon from '@clayui/icon'
import {Liferay} from "../../common/services/liferay/liferay"
import {useTranslation} from "react-i18next"

const spriteMap = Liferay.Icons.spritemap

const RequiredMask = () => {
    const {t} = useTranslation()
    return (
        <>
            <span className="ml-1 reference-mark text-warning">
                <ClayIcon symbol="asterisk" spritemap={spriteMap}/>
            </span>

            <span className="hide-accessible sr-only">
                {t('mandatory')}
            </span>
        </>
    )
}

export default RequiredMask