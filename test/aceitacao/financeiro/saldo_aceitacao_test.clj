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
                                     (receita 2000))
                          (http/post (endereco-para "/transacoes")
                                     (receita 2000))
                          (http/post (endereco-para "/transacoes")
                                     (despesa 3000))

                          (json/parse-string (conteudo "/saldo") true) => {:saldo 1000})

                    (fact "Rejeita uma transação com valor que não é um número" :aceitacao
                          (let [resposta (http/post (endereco-para "/transacoes")
                                                    (receita "mil"))]
                            (:status resposta) => 422))

                    (fact "Rejeita uma transação sem tipo" :aceitacao
                          (let [resposta (http/post (endereco-para "/transacoes")
                                                    (conteudo-como-json {:valor 70}))]
                            (:status resposta) => 422))

                    (fact "Rejeita uma transação com tipo desconhecido" :aceitacao
                          (let [resposta (http/post (endereco-para "/transacoes")
                                                    (conteudo-como-json {:valor 70
                                                                         :tipo "investimento"}))]
                            (:status resposta) => 422 )))