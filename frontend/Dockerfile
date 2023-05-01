FROM node:16.20-alpine
WORKDIR /usr/src/next
COPY . .
RUN npm install
RUN npm run build
EXPOSE 3000
CMD [ "npm" , "run" , "dev"]