FROM nginx:1.17
RUN rm /etc/nginx/conf.d/default.conf
COPY ./client-info.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
ENTRYPOINT ["nginx","-g","daemon off;"]
