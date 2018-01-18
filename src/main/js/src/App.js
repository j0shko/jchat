import React, { Component } from 'react';
import uuid from 'uuid/v4';
import './App.css';

class App extends Component {

  constructor(props) {
    super(props);
    this.state = {
      messages: [],
      input: ""
    };
    this.uuid = uuid();
    this.socket = new WebSocket("ws://localhost:8080/ws-chat");
    this.socket.onmessage = (msg) => {
      this.setState({
        messages: [...this.state.messages, msg.data]
      })
    };
  }

  onInputChange = (e) => {
    this.setState({ input: e.target.value })
  };

  send = (e) => {
    e.preventDefault();
    this.socket.send(this.uuid + "/" + this.state.input);
    this.setState({input : ""});
  };

  isMy = (m) => {
    return m.startsWith(this.uuid);
  };

  text = (m) => {
    return m.slice(m.indexOf("/") + 1);
  };

  render() {
    return (
      <div className="App">
        <main>
          <div className="messages">
            { this.state.messages.map((m, i) => <span key={i} className={this.isMy(m) ? "mine" : ""}>{this.text(m)}</span> )}
          </div>
        </main>
        <form onSubmit={this.send}>
          <input onChange={this.onInputChange} value={this.state.input}/>
          <button type="submit">SEND</button>
        </form>
      </div>
    );
  }
}

export default App;
