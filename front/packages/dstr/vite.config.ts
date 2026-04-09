import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react-swc';
import path from 'path';

export default defineConfig({
  plugins: [react()],
  resolve: {
    alias: {
      '@': path.resolve(__dirname, './src'),
    },
    dedupe: ['react', 'react-dom'],
  },
  optimizeDeps: {
    include: ['react-datepicker', 'date-fns/locale', 'react-select'],
  },
  server: {
    port: 3001,
    proxy: {
      '/api': {
        target: 'http://localhost:30001',
        changeOrigin: true,
      },
      '/websocket': {
        target: 'ws://localhost:26000',
        ws: true,
      },
    },
  },
});
