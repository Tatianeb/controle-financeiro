(ns financeiro.handler-test
  (:require [midje.sweet :refer :all]
            [ring.mock.request :as mock]
            [financeiro.handler :refer :all]))

(facts "Dá um 'Olá mundo!' na rota raiz"
       (let [response (app (mock/request :get "/"))]
         (fact "o status da resposta é 200"
               (:status response) => 200)

         (fact "o texto do corpo é 'Olá mundo!'"
               (:body response) => "Olá mundo!")))

(facts "Rota inválida não existe"
       (let [response (app (mock/request :get "/invalid"))]
         (fact "o código de erro é 404"
               (:status response) => 404)

         (fact "o texto do corpo é ´Recurso não encontrado´"
               (:body response) => "Recurso não encontrado")))

(facts "O saldo inicial é 0"
       (let [response (app (mock/request :get "/saldo"))]
         (fact "O status da resposta é 200"
               (:status response) => 200)
         (fact "O texto do corpo é '0'"
               (:body response) => "0")))