FROM golang:1.17.2-stretch
ADD know-bot know-bot
CMD ["./know-bot"]

