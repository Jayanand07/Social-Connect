import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import tailwindcss from '@tailwindcss/vite'

export default defineConfig({
  plugins: [react(), tailwindcss()],

  build: {
    // Warn if any chunk is over 400KB (helps catch bundle bloat)
    chunkSizeWarningLimit: 400,

    rollupOptions: {
      output: {
        // Split heavy libraries into separate cached chunks.
        // Users only re-download a chunk when THAT library changes,
        // not the whole app bundle.
        manualChunks: {
          // Core React — changes rarely, cached for a long time
          'react-vendor': ['react', 'react-dom', 'react-router-dom'],

          // Framer Motion is ~200KB — isolate it so other changes
          // don't invalidate this chunk
          'motion': ['framer-motion'],

          // All UI component libraries together
          'ui-vendor': [
            '@radix-ui/react-avatar',
            '@radix-ui/react-dialog',
            '@radix-ui/react-dropdown-menu',
            'lucide-react',
            'class-variance-authority',
            'clsx',
            'tailwind-merge',
          ],

          // WebSocket / real-time libraries
          'realtime': ['@stomp/stompjs', 'sockjs-client'],

          // Date utilities
          'utils': ['date-fns', 'axios'],
        },
      },
    },
  },

  // Enable source maps for production debugging (optional but useful)
  // Remove this line if you want smaller builds:
  // build: { sourcemap: true },
})
