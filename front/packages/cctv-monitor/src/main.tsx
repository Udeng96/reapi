import React from 'react';
import ReactDOM from 'react-dom/client';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import App from './App';

import './styles/reset.css';
import './styles/common.css';
import './styles/fontStyle.css';
import './styles/dashContainer.css';
import './styles/cctvContainer.css';
import './styles/changeControlArea.css';
import './styles/treeCustom.css';
import './styles/scrollStyle.css';
import './styles/topBar.css';

const queryClient = new QueryClient();

ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
    <QueryClientProvider client={queryClient}>
      <App />
    </QueryClientProvider>
  </React.StrictMode>,
);
