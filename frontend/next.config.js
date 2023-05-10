/** @type {import('next').NextConfig} */
const nextConfig = {
  experimental: {
    appDir: true,
  },
  images: {
    domains: ["d3geiv4ai2wdw1.cloudfront.net"],
    remotePatterns: [
      {
        protocol: "https",
        hostname: "ai-result-rapidapi.ailabtools.com",
      },
    ],
  },
};

module.exports = nextConfig;
