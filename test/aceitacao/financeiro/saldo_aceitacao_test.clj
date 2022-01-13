(ns financeiro.saldo-aceitacao-test
  (:require [midje.sweet :refer :all]
            [cheshire.core :as json]
            [financeiro.auxiliares :refer :all]
            [clj-http.client :as http]
            [financeiro.db :as db]))

(against-background [(before :facts [(iniciar-servidor porta-padrao) (db/limpar)])
                     (after :facts (parar-servidor))]

                    (fact "O saldo inicial é 0" :aceitacao
                          (json/parse-string (conteudo "/saldo") true) => {:saldo 0})

                    (fact "O saldo é 1000 quando criamos duas receitas de 2000 e uma despesa de 3000"
                          (http/post (endereco-para "/transacoes")
                                     {:content-type :json
                                      :body (json/generate-string {:valor 2000
                                                                   :tipo "receita"})})
                          (http/post (endereco-para "/transacoes")
                                     {:content-type :json
                                      :body (json/generate-string {:valor 2000
                                                                   :tipo "receita"})})
                          (http/post (endereco-para "/transacoes")
                                     {:content-type :json
                                      :body (json/generate-string {:valor 3000
                                                                   :tipo "despesa"})})

                          (json/parse-string (conteudo "/saldo") true) => {:saldo 1000}))