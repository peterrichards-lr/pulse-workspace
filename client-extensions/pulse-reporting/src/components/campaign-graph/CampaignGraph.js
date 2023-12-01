import {useEffect, useState} from 'react';
import {BarElement, CategoryScale, Chart as ChartJS, Legend, LinearScale, Title, Tooltip,} from 'chart.js';
import {Bar} from 'react-chartjs-2';

import '../../common/styles/campaign-graph.scss'

ChartJS.register(
    CategoryScale,
    LinearScale,
    BarElement,
    Title,
    Tooltip,
    Legend
);

const COLORS = [
    '#4dc9f6',
    '#f67019',
    '#f53794',
    '#537bc4',
    '#acc236',
    '#166a8f',
    '#00a950',
    '#58595b',
    '#8549ba',
];

const CampaignGraph = ({data}) => {
    const [datasets, setDatasets] = useState([]);

    useEffect(() => {
        const events = [...new Set(data.map((d) => d.event))];
        const campaignEventCounts = data.reduce((acc, it) => {
            const key = `${it.campaign}:${it.event}`;
            acc[key] = acc[key] === undefined ? 1 : acc[key] + it.count;
            return acc;
        }, {});
        console.debug('events', events);
        console.debug('campaignEventCounts', campaignEventCounts);
        let campaignDatasets = [];
        for (const key in campaignEventCounts) {
            const [campaign, event] = key.split(':');
            campaignDatasets.push({
                backgroundColor: COLORS[events.indexOf(event) % COLORS.length],
                label: event,
                data: Array.of({x: campaign, y: campaignEventCounts[key]}),
                grouped: true,
                stack: event,
                barPercentage: 1
            });
        }
        console.debug('campaignDatasets', campaignDatasets);
        setDatasets(campaignDatasets);
    }, [data]);

    const options = {
        responsive: true,
        plugins: {
            legend: {
                display: false
            },
        },
        layout: {
            padding: 20
        }
    };

    return (
        <main className="chart-container">
            <Bar options={options} data={{datasets}}/>
        </main>
    );
};

export default CampaignGraph;
