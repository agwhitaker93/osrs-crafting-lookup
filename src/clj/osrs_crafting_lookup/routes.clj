(ns osrs-crafting-lookup.routes
  (:require [clojure.java.io :as io]
            [ring.middleware.json :refer [wrap-json-response]]
            [compojure.core :refer [ANY GET PUT POST DELETE routes]]
            [compojure.route :refer [resources]]
            [ring.util.response :refer [response]]
            [osrs-crafting-lookup.components.items :as items]
            [osrs-crafting-lookup.components.details :as details]))

(defn home-routes [endpoint]
  (routes
   (wrap-json-response (GET "/api/items" {{query :query} :params} (items/handle query)))
   (wrap-json-response (GET "/api/details" {{query :query} :params} (details/handle query)))
   (GET "/" _
        (-> "public/index.html"
            io/resource
            io/input-stream
            response
            (assoc :headers {"Content-Type" "text/html; charset=utf-8"})))
   (resources "/")))
