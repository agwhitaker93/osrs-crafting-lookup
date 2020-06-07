(ns osrs-crafting-lookup.components.http-client
  (:require [clj-http.client :as client]))

(defn get-with-retry [url]
  (loop [result (client/get url)
         sleep-seconds 3
         sleep-millis (* sleep-seconds 1000)
         retries 10]
    (if (or (< retries 0) (not (empty? (:body result))))
      result
      (do (printf "Got empty body from url \"%s\", retrying after %s seconds%nresponse: %s%n%n" url sleep-seconds (str result))
          (Thread/sleep sleep-millis)
          (recur url sleep-seconds sleep-millis (dec retries))))))
