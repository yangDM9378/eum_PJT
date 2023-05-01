/** @type {import('next').NextConfig} */
const nextConfig = {
  experimental: {
    appDir: true,
  },
  images: {
    remotePatterns: [
      {
        protocol: "https",
        hostname: "ai-result-rapidapi.ailabtools.com",
      },
    ],
  },
};

module.exports = nextConfig;
