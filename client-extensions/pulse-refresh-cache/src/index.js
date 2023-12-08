import React from 'react'
import {createRoot} from 'react-dom/client'
import './common/styles/index.scss'
import RefreshCache from "../../pulse-refresh-cache/src/components/refresh-cache/RefreshCache";

class WebComponent extends HTMLElement {
    render() {
        createRoot(this).render(
            <RefreshCache />
        );
    }

    connectedCallback() {
        if (!this.rendered) {
            this.render();
            this.rendered = true;
        }
    }
}

const ELEMENT_ID = 'pulse-refresh-cache'

if (!customElements.get(ELEMENT_ID)) {
    customElements.define(ELEMENT_ID, WebComponent)
}