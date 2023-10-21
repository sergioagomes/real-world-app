(ns real-world-api.core
  (:require [real-world-api.config :as config]
            [io.pedestal.http :as http]
            [io.pedestal.http.route :as route]))

(defn respond-hello [request]
  {:status 200 
   :body "Hello, World!!"})

;; create routes
(def routes
  (route/expand-routes
   #{"/greet" :get respond-hello :route-name :greet}))

;; create server
(defn create-server []
  (http/create-server
   {::http/routes routes
    ::http/type :jetty
    ::http/port 8890}))

;; start de server
(defn start []
  (http/start (create-server)))


(defn -main 
  []
  (let [config (config/read-config)]
    (println "Start Real World App With Config" config)
    (start)
    (println "Live")))