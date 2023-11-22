import React from 'react';
import {createRoot} from 'react-dom/client';

import UserJourney from './components/user-journey/UserJourney';

import './common/styles/index.scss';

const App = ({clientExternalReferenceCode}) => {
    return (
        <div>
            <UserJourney clientExternalReferenceCode={clientExternalReferenceCode}/>
        </div>
    );
};

class WebComponent extends HTMLElement {
    constructor() {
        super();
        this.root = createRoot(this);
    }

    connectedCallback() {
        this.root.render(
            <App
                clientExternalReferenceCode={this.getAttribute(
                    'clientexternalreferencecode'
                )}
            />
        );
    }
}

const ELEMENT_ID = 'user-journey';

if (!customElements.get(ELEMENT_ID)) {
    customElements.define(ELEMENT_ID, WebComponent);
}
