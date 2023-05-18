FROM node:16.20-alpine
WORKDIR /usr/src/next
COPY . .
RUN npm run build
RUN npm install
EXPOSE 3000
CMD [ "npm" , "start"]
