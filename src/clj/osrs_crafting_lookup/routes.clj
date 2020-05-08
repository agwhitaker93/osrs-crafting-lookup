(ns osrs-crafting-lookup.routes
  (:require [clojure.java.io :as io]
            [compojure.core :refer [ANY GET PUT POST DELETE routes]]
            [compojure.route :refer [resources]]
            [ring.util.response :refer [response]]
            [osrs-crafting-lookup.components.search :as search]))

(defn home-routes [endpoint]
  (routes
   (GET "/api/search" {{query :query} :params} (search/handle query))
   (GET "/" _
        (-> "public/index.html"
            io/resource
            io/input-stream
            response
            (assoc :headers {"Content-Type" "text/html; charset=utf-8"})))
   (resources "/")))
