//
//  ClientManager.swift
//  Exodus
//
//  Created by iverbrugge25 on 5/24/24.
//

import Foundation

public class ClientManager {
    public var client:Client = Client(ip:"localhost")
    
    private init() {
        
    }
    
    static let shared = ClientManager()

}
