(ns osrs-crafting-lookup.components.http-client
  (:require [clj-http.client :as client]))

(defn get-with-retry [url]
  (let [result (client/get url)
        sleep-seconds 30
        sleep-millis (* sleep-seconds 1000)]
    (if (empty? (:body result))
      (do (printf "Got empty body from url \"%s\", retrying after %s seconds%nresponse: %s%n%n" url sleep-seconds (str result))
          (Thread/sleep sleep-millis)
          (get-with-retry url))
      result)))
