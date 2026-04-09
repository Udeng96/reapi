import React from 'react';
import ReactDOM from 'react-dom/client';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import App from './App';

import './styles/reset.css';
import './styles/fontStyle.css';
import './styles/common.css';
import './styles/menuContainer.css';
import './styles/dashContainer.css';
import './styles/gis.css';
import './styles/gisIcon.css';
import './styles/tooltip.css';
import './styles/doorlockPopup.css';
import './styles/historyPopup.css';
import './styles/cctvPopup.css';
import './styles/modalPopup.css';
import './styles/scrollStyle.css';

const queryClient = new QueryClient();

ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
    <QueryClientProvider client={queryClient}>
      <App />
    </QueryClientProvider>
  </React.StrictMode>,
);
