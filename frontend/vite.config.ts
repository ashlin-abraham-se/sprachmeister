import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    // Forward API calls to the Spring Boot backend during development,
    // so the frontend can use relative URLs and no CORS config is needed.
    proxy: {
      '/api': 'http://localhost:8080',
    },
  },
  build: {
    // Production build lands in Spring Boot's static folder, so
    // `npm run build` + `mvn spring-boot:run` serves the whole app
    // from http://localhost:8080 as a single jar.
    outDir: '../src/main/resources/static',
    emptyOutDir: true,
  },
})
