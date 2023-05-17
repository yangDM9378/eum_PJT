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
      {
        protocol: "https",
        hostname: "d3geiv4ai2wdw1.cloudfront.net",
      },
    ],
  },
};

module.exports = nextConfig;
