import React from 'react'
import {createRoot} from 'react-dom/client'
import './common/styles/index.scss'
import CreateCampaignForm from "./components/campaign-form/CreateCampaignForm"

class WebComponent extends HTMLElement {
    static get observedAttributes() {
        return ['campaignstatuslisttypeerc', 'defaultcampaignstatus'];
    }

    attributeChangedCallback(name, oldValue, newValue) {
        console.debug(`The attribute ${name} was updated.`);
        switch (name) {
            case 'campaignstatuslisttypeerc':
            case 'defaultcampaignstatus':
                if (!newValue || newValue === oldValue) return;
                this.render();
                return;
            default:
                return;
        }
    }

    render() {
        createRoot(this).render(
            <CreateCampaignForm
                campaignStatusListTypeErc={this.getAttribute('campaignstatuslisttypeerc')}
                defaultCampaignStatus={this.getAttribute('defaultcampaignstatus')}
            />
        );
    }

    connectedCallback() {
        if (!this.rendered) {
            this.render();
            this.rendered = true;
        }
    }
}

const ELEMENT_ID = 'pulse-create-campaign'

if (!customElements.get(ELEMENT_ID)) {
    customElements.define(ELEMENT_ID, WebComponent)
}