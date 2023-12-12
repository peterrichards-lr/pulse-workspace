import CreateCampaignForm from "./campaign-form/CreateCampaignForm"
import RefreshCache from "./refresh-cache/RefreshCache"
import CheckRefreshStatus from "./refresh-cache/CheckRefreshStatus";

const App = ({route, ...props}) => {
    if (route === 'create-campaign') {
        return <CreateCampaignForm {...props} />
    }
    if (route === 'refresh-cache') {
        return <RefreshCache {...props} />
    }
    if (route === 'check-refresh-status')
        return <CheckRefreshStatus {...props} />
    return <RefreshCache {...props} />
}

export default App