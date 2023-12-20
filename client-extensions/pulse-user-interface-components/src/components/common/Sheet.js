const Sheet = ({
                   children,
                   description,
                   footer,
                   title,
               }) => {
    return (
        <div className="sheet sheet-lg">
            <div className="sheet-header">
                <h2 className="sheet-title">{title}</h2>

                <div className="sheet-text">{description}</div>
            </div>

            <div className="sheet-section">{children}</div>

            <div className="sheet-footer sheet-footer-btn-block-sm-down">
                {footer}
            </div>
        </div>
    )
}

export default Sheet;