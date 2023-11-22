import {React, useEffect, useMemo, useState} from 'react';
import {exportToCsv, exportToPdf, exportToXlsx} from './exportUtils';

import 'react-data-grid/lib/styles.css';
import DataGrid from 'react-data-grid';

import TimeAgo from 'javascript-time-ago'
import en from 'javascript-time-ago/locale/en'

import '../../common/styles/campaign-data-grid.scss'

TimeAgo.addDefaultLocale(en)
const timeAgo = new TimeAgo('en-US');

const DateFormatter = ({value}) => {
    const dateObj = Date.parse(value);
    return <>{timeAgo.format(dateObj)}</>;
};

const rowKeyGetter = (row) => {
    return row.key;
};

const getComparator = (sortColumn) => {
    switch (sortColumn) {
        case 'campaign':
        default:
            throw new Error(`unsupported sortColumn: "${sortColumn}"`);
    }
};

const CampaignDataGrid = ({rows}) => {
    const [columns, setColumns] = useState([]);
    const [sortColumns, setSortColumns] = useState([]);

    useEffect(() => {
        setColumns([
            {key: 'campaign', name: 'Campaign'},
            {key: 'sourceMedium', name: 'Source / Medium'},
            {key: 'event', name: 'Event'},
            {key: 'count', name: 'Count'},
            {
                key: 'lastUpdated',
                name: 'Last Update',
                formatter(props) {
                    return <DateFormatter value={props.row.lastUpdated}/>;
                },
            },
        ]);
    }, [rows]);

    const sortedRows = useMemo(() => {
        if (rows === undefined) return [];
        if (sortColumns.length === 0) return rows;

        return [...rows].sort((a, b) => {
            for (const sort of sortColumns) {
                const comparator = getComparator(sort.columnKey);
                const compResult = comparator(a, b);
                if (compResult !== 0) {
                    return sort.direction === 'ASC' ? compResult : -compResult;
                }
            }
            return 0;
        });
    }, [rows, sortColumns]);

    let gridElement = (
        <DataGrid
            rowKeyGetter={rowKeyGetter}
            columns={columns}
            rows={sortedRows}
            defaultColumnOptions={{
                sortable: true,
                resizable: true,
            }}
            sortColumns={sortColumns}
            onSortColumnsChange={setSortColumns}
            className="fill-grid"
        />
    );

    return (
        <main className="grid-container">
            <div className="toolbar">
                <ExportButton
                    onExport={() => exportToCsv(gridElement, 'UserJourney.csv')}
                >
                    Export to CSV
                </ExportButton>
                <ExportButton
                    onExport={() => exportToXlsx(gridElement, 'UserJourney.xlsx')}
                >
                    Export to XSLX
                </ExportButton>
                <ExportButton
                    onExport={() => exportToPdf(gridElement, 'UserJourney.pdf')}
                >
                    Export to PDF
                </ExportButton>
            </div>
            {gridElement}
        </main>
    );
};

function ExportButton({onExport, children}) {
    const [exporting, setExporting] = useState(false);
    return (
        <button
            disabled={exporting}
            onClick={async () => {
                setExporting(true);
                await onExport();
                setExporting(false);
            }}
        >
            {exporting ? 'Exporting' : children}
        </button>
    );
}

export default CampaignDataGrid;
