(ns real-world-api.core
  (:require [real-world-api.config :as config]
            [io.pedestal.http :as http]
            [io.pedestal.http.route :as route]
            [real-world-api.components.example-component :as example-component]
            [com.stuartsierra.component :as component]))

(defn respond-hello [request]
  {:status 200 
   :body "Hello, World!!"})

;; create routes
(def routes
  (route/expand-routes
   #{["/greet" :get respond-hello :route-name :greet]}))

;; create server
(defn create-server 
  [config]
  (http/create-server
   {::http/routes routes
    ::http/type :jetty
    ::http/join? false
    ::http/port (-> config :server :port)}))

;; start de server
(defn start 
  [config]
  (http/start (create-server config)))

;; define a function to use the component
(defn real-world-system
  [config]
  (component/system-map
   :example-component (example-component/new-example-component 
                       config)))


(defn -main 
  []
  (let [system (-> (config/read-config)
                   (real-world-system)
                   (component/start-system))] 
    (println "Start Real World App With Config" )
    (.addShutdownHook
     (Runtime/getRuntime)
     (new Thread #(component/stop-system system)))))