//
//  Client.swift
//  Exodus
//
//  Created by Isaac Verbrugge on 2/29/24.
//

import Foundation
import Network
public class Client: NSObject, StreamDelegate  {
    
    var serverAddress: CFString
    let serverPort: UInt32 = 25501
    
    private var inputStream: InputStream!
    private var outputStream: OutputStream!
    private var connecting:Bool
    
    public var numberPlayers:Int32 = 0
    public var connected:Bool = false
    
    
    init(ip:String) {
        serverAddress = ip as CFString
        self.connecting = false
        self.connected = false
        super.init()
        connect()
    }
    
    
    
    func connect() {
        self.connecting = true
        print("connecting...")
        var readStream:  Unmanaged<CFReadStream>?
        var writeStream: Unmanaged<CFWriteStream>?
        
        CFStreamCreatePairWithSocketToHost(nil, self.serverAddress, self.serverPort, &readStream, &writeStream)
        
        self.inputStream = readStream!.takeRetainedValue()
        self.outputStream = writeStream!.takeRetainedValue()
        
        self.inputStream.delegate = self
        self.outputStream.delegate = self
        
        self.inputStream.schedule(in: .current, forMode: RunLoop.Mode.default)
        self.outputStream.schedule(in: .current, forMode: RunLoop.Mode.default)
        
        self.inputStream.open()
        self.outputStream.open()
    }
    
    
    
    func closeConnection(){
        self.connecting = false
        self.inputStream.close()
        self.outputStream.close()
    }
    
    
    
    public func sendMessagePacket(packetID:Int, message:String){
        if(self.connected){
            print("sending message...")
            sendInt32(number: 100) // status
            sendInt32(number: packetID) // packetID
            sendString(string: message) // message
        }
    }
    
    public func sendPlayerRequestPacket(packetID:Int){
        if(self.connected){
            print("sending player request...")
            sendInt32(number: 100) // status
            sendInt32(number: packetID) // packetID
            sendInt32(number: -1) // number that are on? invalid from client? idk
        }
    }
    
    
    
    func readInt32() -> Int32? {
        // Buffer to hold the 4 bytes of the integer
        var buffer = [UInt8](repeating: 0, count: 4)
        
        // Read exactly 4 bytes from the input stream
        let bytesRead = self.inputStream.read(&buffer, maxLength: 4)
        
        // Check if we read the correct number of bytes
        guard bytesRead == 4 else {
            print("Error: Expected to read 4 bytes, but read \(bytesRead) bytes")
            return nil
        }
        
        // Convert the buffer to a 32-bit integer in big-endian format
        let bigEndianValue = buffer.withUnsafeBytes {
            $0.load(as: UInt32.self)
        }.bigEndian
        
        // Convert UInt32 to Int32
        return Int32(bigEndianValue)
    }
    
//    func getNumberPlayers(length: Int)-> Int {
//        var buffer = [UInt8](repeating: 0, count: length)
//        var bytes = inputStream?.read(&buffer, maxLength: length)
//        let data = NSMutableData(bytes: &buffer, length: bytes!)
//
//        let read = inputStream!.read(&buffer, maxLength: length)
//        bytes! += read
//        data.append(&buffer, length: read)
//
////        var str:NSString! = ""
////        str = NSString(bytes: data.bytes, length: bytes!, encoding: String.Encoding.utf8.rawValue)
//
//        return num as Int
//    }
    
    
    
    func sendString(string: String) {
        let string = string + "\r\n"
        let encodedDataArray = [UInt8](string.utf8)
        outputStream?.write(encodedDataArray, maxLength: encodedDataArray.count)
    }
    
    
    
    func sendInt32(number: Int) {
        var number = Int32(number).bigEndian
        let numberData = Data(bytes: &number, count: MemoryLayout<Int32>.size)
        outputStream?.write([UInt8](numberData), maxLength: MemoryLayout<Int32>.size)
    }
    
    
    
    public func stream(_ stream: Stream, handle eventCode: Stream.Event) {
        if stream === inputStream {
            switch eventCode {
            case .openCompleted:
                print("Connection opened successfully.")
                self.connected = true
            case Stream.Event.errorOccurred:
                print("Input: ErrorOccurred")
            case Stream.Event.openCompleted:
                print("Input: Stream Opened")
            case Stream.Event.hasBytesAvailable:
                readInt32()
                if(readInt32() == 4){
                    self.numberPlayers = readInt32()!
                }
                
            default:
                break
            }
        }
        else if stream === outputStream {
            switch eventCode {
            case Stream.Event.errorOccurred:
                print("Output: ErrorOccurred")
            case Stream.Event.openCompleted:
                print("Output: Stream Opened")
            case Stream.Event.hasSpaceAvailable:
                break
            default:
                break
            }
        }
        
    }
    
    
    
}
