//
//  ViewController.swift
//  Exodus
//
//  Created by iverbhirugge25 on 1/18/24.
//

import UIKit

class ViewController: UIViewController {
    var client:Client = ClientManager.shared.client
    var currentMessage:String = ""
    @IBOutlet weak var packetTextField: UITextField!
    
    
    
    override func viewDidLoad() {
        client.sendMessagePacket(packetID: 3, message: "Connected")
        super.viewDidLoad()
    }
    
    
    
    
    @IBAction func sendPacket(_ sender: Any) {
        client.sendMessagePacket(packetID: 3, message: currentMessage)
    }
    
    
    
    @IBAction func updateMessage(_ sender: Any) {
        currentMessage = packetTextField.text!
    }
    
    
    
}

