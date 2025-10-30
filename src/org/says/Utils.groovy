package org.says


class Utils implements Serializable {
    staric String shortCommit(String hash) {
        
        if (hash == null) return ''
        return hash.take(7)

    }
}