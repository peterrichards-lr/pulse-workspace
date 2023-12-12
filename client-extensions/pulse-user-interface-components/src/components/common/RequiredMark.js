import ClayIcon from '@clayui/icon';
import {Liferay} from "../../common/services/liferay/liferay";

const spriteMap = Liferay.Icons.spritemap;

const RequiredMask = () => {
    return (
        <>
            <span className="ml-1 reference-mark text-warning">
                <ClayIcon symbol="asterisk" spritemap={spriteMap}/>
            </span>

            <span className="hide-accessible sr-only">
                {Liferay.Language.get('mandatory')}
            </span>
        </>
    );
}

export default RequiredMask