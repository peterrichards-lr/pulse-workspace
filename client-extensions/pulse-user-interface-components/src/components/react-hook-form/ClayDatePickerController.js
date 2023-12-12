import {Controller} from "react-hook-form"
import ClayForm from '@clayui/form'
import ClayDatePicker from "@clayui/date-picker"

const ClayDatePickerController = ({
                                      name,
                                      label,
                                      control,
                                      ...otherProps
                                  }) => {
    return (
        <ClayForm.Group>
            <label htmlFor={name}>{label}</label>
            <Controller
                control={control}
                name={name}
                render={({field: {onChange, onBlur, value, ref}}) => (
                    <ClayDatePicker
                        onChange={onChange}
                        onBlur={onBlur}
                        value={value}
                        ref={ref}
                        {...otherProps}
                    />
                )}
            />
        </ClayForm.Group>
    )
}

export default ClayDatePickerController