import {Controller} from "react-hook-form"
import ClayForm, {ClaySelect} from '@clayui/form'
import RequiredMark from "../common/RequiredMark";

const ClaySelectController = ({
                                  name,
                                  label,
                                  control,
                                  required = false,
                                  children,
                                  ...otherProps
                              }) => {
    return (
        <ClayForm.Group>
            <label htmlFor={name}>{label}
                {required &&
                    <RequiredMark/>
                }
            </label>
            <Controller
                control={control}
                name={name}
                rules={{required}}
                render={({field: {onChange, onBlur, value, ref}}) => (
                    <ClaySelect
                        onChange={onChange}
                        onBlur={onBlur}
                        value={value}
                        ref={ref}
                        {...otherProps}
                    >
                        {children}
                    </ClaySelect>)}
            />
        </ClayForm.Group>
    )
}

export default ClaySelectController