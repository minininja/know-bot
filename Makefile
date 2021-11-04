all: build #test

SHELL := /bin/bash
BINARY = know-bot
# BIN_DIR = $(shell echo $${GOPATH:-~/go} | awk -F':' '{ print $$1 "/bin"}')
BIN_DIR = .

GOFLAGS :=
VERSION_STR = ""

.PHONY: depend format unit coverage build build_mac build_linux clean

depend:
	go get golang.org/x/tools/cmd/goimports
	go get github.com/golang/lint/golint
	go get github.com/onsi/ginkgo/ginkgo
	go get github.com/alecthomas/gometalinter
	gometalinter --install
	go get github.com/golang/dep/cmd/dep
	dep ensure

format:
	gofmt -w -s main.go
	goimports -w main.go

lint:
	#gometalinter --config=gometalinter.config -s vendor ./...
	gometalinter -s vendor ./...

unit: format
	ginkgo -r -cover -coverprofile=coverage.out pkg/add

coverage: unit
	go tool cover -func=pkg/add/coverage.out

end2end:
	ginkgo -r end2end

build: format
	go build -tags '$(BINARY)' $(GOFLAGS) -o ${BIN_DIR}/${BINARY} -ldflags $(VERSION_STR)

build_mac: format
	env GOOS=darwin GOARCH=amd64 go build -tags '$(BINARY)' $(GOFLAGS) -o ${BIN_DIR}/${BINARY}_mac -ldflags $(VERSION_STR)

build_linux: format
	env GOOS=linux GOARCH=amd64 go build -tags '$(BINARY)' $(GOFLAGS) -o ${BIN_DIR}/${BINARY}_linux -ldflags $(VERSION_STR)

# docker: build
#     docker build . -t mikej091/${BINARY}:latest
#     docker push mikej091/${BINARY}:latest

clean:
	rm -f $(BINARY)_mac $(BINARY)_linux $(BIN_DIR)/$(BINARY) ./coverage.out