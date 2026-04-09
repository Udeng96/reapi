import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react-swc';
import path from 'path';

export default defineConfig({
  plugins: [react()],
  base: '/api/cctvMonitor/',
  resolve: {
    alias: {
      '@': path.resolve(__dirname, './src'),
    },
  },
  server: {
    port: 3003,
    proxy: {
      '/api/cctv/': {
        target: 'http://localhost:30003',
        changeOrigin: true,
      },
    },
  },
  build: {
    outDir: 'dist',
  },
});
