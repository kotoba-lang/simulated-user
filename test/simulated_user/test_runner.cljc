(ns simulated-user.test-runner
  (:require [clojure.test :as t]
            [simulated-user.model-test]
            [simulated-user.eval-test]))

(defn -main [& _]
  (let [result (t/run-tests 'simulated-user.model-test
                            'simulated-user.eval-test)]
    (when (pos? (+ (:fail result) (:error result)))
      #?(:clj (System/exit 1)
         :cljs (js/process.exit 1)))))
