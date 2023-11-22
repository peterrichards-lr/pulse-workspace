import {cloneElement} from 'react';

export async function exportToCsv(gridElement, fileName) {
    const {head, body, foot} = await getGridContent(gridElement);
    const content = [...head, ...body, ...foot]
        .map((cells) => cells.map(serialiseCellValue).join(','))
        .join('\n');

    downloadFile(
        fileName,
        new Blob([content], {type: 'text/csv;charset=utf-8;'})
    );
}

export async function exportToXlsx(gridElement, fileName) {
    const [{utils, writeFile}, {head, body, foot}] = await Promise.all([
        import('xlsx'),
        getGridContent(gridElement),
    ]);
    const wb = utils.book_new();
    const ws = utils.aoa_to_sheet([...head, ...body, ...foot]);
    utils.book_append_sheet(wb, ws, 'Sheet 1');
    writeFile(wb, fileName);
}

export async function exportToPdf(gridElement, fileName) {
    const [{jsPDF}, autoTable, {head, body, foot}] = await Promise.all([
        import('jspdf'),
        (await import('jspdf-autotable')).default,
        await getGridContent(gridElement),
    ]);
    const doc = new jsPDF({
        orientation: 'l',
        unit: 'px',
    });

    autoTable(doc, {
        head,
        body,
        foot,
        horizontalPageBreak: true,
        styles: {cellPadding: 1.5, fontSize: 8, cellWidth: 'wrap'},
        tableWidth: 'wrap',
    });
    doc.save(fileName);
}

async function getGridContent(gridElement) {
    const {renderToStaticMarkup} = await import('react-dom/server');
    const grid = document.createElement('div');
    grid.innerHTML = renderToStaticMarkup(
        cloneElement(gridElement, {
            enableVirtualization: false,
        })
    );

    return {
        head: getRows('.rdg-header-row'),
        body: getRows('.rdg-row:not(.rdg-summary-row)'),
        foot: getRows('.rdg-summary-row'),
    };

    function getRows(selector) {
        return Array.from(grid.querySelectorAll(selector)).map((gridRow) => {
            return Array.from(gridRow.querySelectorAll('.rdg-cell')).map(
                (gridCell) => gridCell.innerText
            );
        });
    }
}

function serialiseCellValue(value) {
    if (typeof value === 'string') {
        const formattedValue = value.replace(/"/g, '""');
        return formattedValue.includes(',')
            ? `"${formattedValue}"`
            : formattedValue;
    }
    return value;
}

function downloadFile(fileName, data) {
    const downloadLink = document.createElement('a');
    downloadLink.download = fileName;
    const url = URL.createObjectURL(data);
    downloadLink.href = url;
    downloadLink.click();
    URL.revokeObjectURL(url);
}
