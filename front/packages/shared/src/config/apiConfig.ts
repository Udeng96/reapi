export interface ApiConfig {
  baseUrl: string;
  wsUrl: string;
  streamUrl: string;
}

export function createApiConfig(): ApiConfig {
  const isDev = import.meta.env.DEV;
  return {
    baseUrl: isDev ? 'http://localhost:22222/api' : '/api',
    wsUrl: isDev ? 'ws://localhost:26000/websocket' : `ws://${location.host}/api-ws`,
    streamUrl: isDev ? 'http://localhost:8090' : '/stream',
  };
}
