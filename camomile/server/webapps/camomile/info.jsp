<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
<head>
  <title>info</title>
  <style type="text/css">
    @import URL("css/global.css");
  </style>
</head>

<body onUnload="">
    <table width="100%">
      <tr>
        <td bgcolor="#259225" align="center">
          <h2><font color="white"><b>Camomile alpha1b</b></font></h2>
        </td>
      </tr>
      <tr><td>&nbsp;</td></tr>
    </table>
    <table width="100%">
      <tr>
        <td bgcolor="#3366cc" align="center">
          <font color="white"><b>GET /rest/{connection}/_sql</b></font>
        </td>
      </tr>
      <tr>
        <td>
          <b>Request:</b><br/>
        </td>
      </tr>
      <tr>
        <td bgcolor="#eeeeee">
          Content-Type: <b>application/json</b><br/>
          { "SQL": "SQL select statement" }
        </td>
      </tr>
      <tr>
        <td>
          <b>Response:</b><br/>
        </td>
      </tr>
      <tr>
        <td bgcolor="#eeeeee">
          Content-Type: <b>application/json</b><br/>
          { ??? }
        </td>
      </tr>
      <tr><td colspan="2">&nbsp;</td></tr>
      <tr>
        <td bgcolor="#3366cc" align="center">
          <font color="white"><b>GET /rest/{connection}/_sql/{limit}</b></font>
        </td>
      </tr>
      <tr>
        <td>
          <b>Request:</b><br/>
        </td>
      </tr>
      <tr>
        <td bgcolor="#eeeeee">
          Content-Type: <b>application/json</b><br/>
          { "SQL": "SQL select statement" }
        </td>
      </tr>
      <tr>
        <td>
          <b>Response:</b><br/>
        </td>
      </tr>
      <tr>
        <td bgcolor="#eeeeee">
          Content-Type: <b>application/json</b><br/>
          { ??? }
        </td>
      </tr>
      <tr><td colspan="2">&nbsp;</td></tr>
      <tr>
        <td bgcolor="#3366cc" align="center">
          <font color="white"><b>POST /rest/{connection}/_sql</b></font>
        </td>
      </tr>
      <tr>
        <td>
          <b>Request:</b><br/>
        </td>
      </tr>
      <tr>
        <td bgcolor="#eeeeee">
          Content-Type: <b>application/json</b><br/>
          { "SQL": "SQL execute statement e.g. DELETE, UPDATE, INSERT" }
        </td>
      </tr>
      <tr>
        <td>
          <b>Response:</b><br/>
        </td>
      </tr>
      <tr>
        <td bgcolor="#eeeeee">
          Content-Type: <b>application/json</b><br/>
          { ??? }
        </td>
      </tr>
      <tr><td colspan="2">&nbsp;</td></tr>
    </table>
    <table width="100%">
      <tr><td><hr size="2" noshade="noshade" /></td></tr>
      <tr>
        <td align="center">
          <span class="subscript">Copyright &copy; 2011 William Wragg</span><br/>
          <span class="subscript">All Rights Reserved</span> <br/>
          &nbsp;
        </td>
      </tr>
    </table>
</body>
</html>
