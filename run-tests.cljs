(ns run-tests
  (:require [simulated-user.model-test]
            [simulated-user.eval-test]
            [cljs.test :as t]))

(let [result (t/run-tests 'simulated-user.model-test
                          'simulated-user.eval-test)]
  (when (pos? (+ (:fail result 0) (:error result 0)))
    (js/process.exit 1)))
