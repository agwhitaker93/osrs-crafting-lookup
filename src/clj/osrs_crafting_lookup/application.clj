(ns osrs-crafting-lookup.application
  (:gen-class)
  (:require [com.stuartsierra.component :as component]
            [system.components.endpoint :refer [new-endpoint]]
            [system.components.handler :refer [new-handler]]
            [system.components.middleware :refer [new-middleware]]
            [system.components.jetty :refer [new-web-server]]
            [osrs-crafting-lookup.components.server-info :refer [server-info]]
            [osrs-crafting-lookup [config :refer [config]]
             [routes :refer [home-routes]]]
            [osrs-crafting-lookup.database :as database]))

(defn app-system [config]
  (component/system-map
   :routes     (new-endpoint home-routes)
   :middleware (new-middleware {:middleware (:middleware config)})
   :handler    (-> (new-handler)
                   (component/using [:routes :middleware]))
   :http       (-> (new-web-server (:http-port config))
                   (component/using [:handler]))
   :server-info (server-info (:http-port config))))

(defn -main [& _]
  (doall (database/init))
  (let [config (config)]
    (-> config
        app-system
        component/start)))
